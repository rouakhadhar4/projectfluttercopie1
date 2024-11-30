package com.example.demo.controller;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Departement;
import com.example.demo.entity.Employe;
import com.example.demo.entity.Projet;
import com.example.demo.service.EmployeService;
import com.example.demo.repository.DepartementRepository;
import com.example.demo.repository.ProjetRepository;
import com.example.demo.repository.EmployeRepository;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
public class EmployeController {

    private final EmployeService employeService;
    private final DepartementRepository departementRepository;
    private final ProjetRepository projetRepository;
    private final EmployeRepository employeRepository;

    // Constructor injection
    public EmployeController(EmployeService employeService,
                             DepartementRepository departementRepository,
                             ProjetRepository projetRepository,
                             EmployeRepository employeRepository) {
        this.employeService = employeService;
        this.departementRepository = departementRepository;
        this.projetRepository = projetRepository;
        this.employeRepository = employeRepository;
    }

    // Get all employees
    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeService.getAllEmployes();
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public Employe getEmployeById(@PathVariable Long id) {
        return employeService.getEmployeById(id);
    }

    // Create new employee
    @PostMapping
    public ResponseEntity<Employe> createEmploye(@RequestBody Employe employe) {
        // Fetch the departement and projet by their ids
        Departement departement = departementRepository.findById(employe.getDepartement().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Departement not found"));

        Projet projet = projetRepository.findById(employe.getProjet().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Projet not found"));

        // Set the departement and projet to the employe
        employe.setDepartement(departement);
        employe.setProjet(projet);

        // Save the employe
        Employe savedEmploye = employeRepository.save(employe);

        // Return the saved employe with the full departement and projet details
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmploye);
    }

    // Update existing employee
    @PutMapping("/{id}")
    public Employe updateEmploye(@PathVariable Long id, @RequestBody Employe employe) {
        employe.setId(id);
        return employeService.saveEmploye(employe);
    }

    // Delete employee by ID
    @DeleteMapping("/{id}")
    public void deleteEmploye(@PathVariable Long id) {
        employeService.deleteEmploye(id);
    }
}
