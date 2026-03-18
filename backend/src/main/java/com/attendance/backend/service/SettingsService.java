package com.attendance.backend.service;

import com.attendance.backend.entity.Settings;
import com.attendance.backend.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    @Autowired
    private SettingsRepository settingsRepository;

    public Settings getSettings() {
        return settingsRepository.findAll().stream().findFirst().orElse(new Settings());
    }

    public Settings saveSettings(Settings settings) {
        settingsRepository.deleteAll(); // Only one settings object in the system
        return settingsRepository.save(settings);
    }
}
