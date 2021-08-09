package com.mfurst.mf0721.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Class for ToolTypes
 * This class contains the information required for billing a tool type
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ToolType {
    /**
     * Unique type identifier
     */
    private String type;
    /**
     * Rate to charge per day for renting this tool
     */
    private BigDecimal charge;
    /**
     * Flag for if customer should be charged for renting this on weekdays
     */
    private boolean weekday;
    /**
     * Flag for if customer should be charged for renting this on weekends
     */
    private boolean weekend;
    /**
     * Flag for if customer should be charged on holidays
     */
    private boolean holiday;
}
