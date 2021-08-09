package com.mfurst.mf0721.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Class for storing holiday information
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {
    /**
     * Type of the holiday
     * Either: closest-weekday, next-monday
     */
    private String type;
    /**
     * Month value of the holiday
     */
    private int month;
    /**
     * Day value of the holiday
     */
    private int day;
}
