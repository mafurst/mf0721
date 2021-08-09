package com.mfurst.mf0721.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to store data about tools
 * It keeps track of the tools available for customers to rent
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    /**
     * Unique identifying code for the tool
     */
    private String code;
    /**
     * Type of the tool, matches a unique tool type
     */
    private String type;
    /**
     * The brand of the tool
     */
    private String brand;
}
