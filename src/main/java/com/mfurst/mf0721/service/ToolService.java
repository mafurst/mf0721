package com.mfurst.mf0721.service;

import com.mfurst.mf0721.exception.ErrorCode;
import com.mfurst.mf0721.exception.ToolManagementException;
import com.mfurst.mf0721.model.ToolInformation;
import com.mfurst.mf0721.model.dto.Tool;
import com.mfurst.mf0721.model.dto.ToolType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service is used for finding tools and tool types
 */
@Service
public class ToolService {
    @Autowired
    private List<Tool> tools;

    @Autowired
    private List<ToolType> toolTypes;

    /**
     * Search through the tools and find the matching tool for the
     * provided tool code
     * @param toolCode code to search for
     * @throws ToolManagementException when tool code is invalid
     * @throws ToolManagementException when tool code has no matches
     * @return
     */
    public ToolInformation getToolByCode(String toolCode) throws ToolManagementException{
        if (toolCode == null || toolCode.isBlank()) {
            throw new ToolManagementException(ErrorCode.TOOL0001);
        }
        Tool tool = tools.stream()
                .filter(t -> t.getCode().equalsIgnoreCase(toolCode))
                .findFirst()
                .orElseThrow(() -> new ToolManagementException(ErrorCode.TOOL0002, toolCode));

        ToolType type = toolTypes.stream()
                .filter(t -> t.getType().equalsIgnoreCase(tool.getType()))
                .findFirst()
                .orElseThrow(() -> new ToolManagementException(ErrorCode.TOOL0003, toolCode));

        return new ToolInformation(tool.getCode(), tool.getBrand(), type);
    }
}
