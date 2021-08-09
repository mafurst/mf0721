package com.mfurst.mf0721.model;

import com.mfurst.mf0721.model.dto.ToolType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This class is used for storing the information about a tool
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ToolInformation {
    /**
     * Unique tool identifier code
     */
    private String toolCode;

    /**
     * Brand of the tool
     */
    private String toolBrand;

    /**
     * Type information about this tool
     */
    private ToolType toolType;
}
