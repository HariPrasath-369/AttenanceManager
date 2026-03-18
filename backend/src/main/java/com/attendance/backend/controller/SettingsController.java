package com.attendance.backend.controller;

import com.attendance.backend.entity.Settings;
import com.attendance.backend.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@PreAuthorize("isAuthenticated()")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public ResponseEntity<Settings> getSettings() {
        return ResponseEntity.ok(settingsService.getSettings());
    }

    @PostMapping
    public ResponseEntity<Settings> saveSettings(@RequestBody Settings settings) {
        return ResponseEntity.ok(settingsService.saveSettings(settings));
    }
}
