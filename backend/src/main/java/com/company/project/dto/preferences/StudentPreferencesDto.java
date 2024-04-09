package com.company.project.dto.preferences;

import java.util.List;

public record StudentPreferencesDto(Long id, String email, List<SinglePreference> preferences) {

}
