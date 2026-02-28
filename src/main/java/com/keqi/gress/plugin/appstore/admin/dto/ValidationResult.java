package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Validation Result
 * Contains the result of validating a rule
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {
    
    /**
     * Whether the validation passed
     */
    private Boolean valid;
    
    /**
     * List of validation errors (empty if valid)
     */
    private List<String> errors;
}
