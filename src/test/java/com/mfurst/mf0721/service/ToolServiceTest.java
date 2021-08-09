package com.mfurst.mf0721.service;

import com.mfurst.mf0721.exception.ErrorCode;
import com.mfurst.mf0721.exception.ToolManagementException;
import com.mfurst.mf0721.model.ToolInformation;
import com.mfurst.mf0721.model.dto.Tool;
import com.mfurst.mf0721.model.dto.ToolType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
class ToolServiceTest {

    private ToolService toolService;

    @BeforeEach
    void setUp() {
        toolService = new ToolService();
    }

    /**
     * Tool search should fail when no tool code is provided
     */
    @Test
    void getToolByCodeShouldFailIfToolCodeIsNullOrEmpty() {
        try {
            toolService.getToolByCode(null);
            fail("Get tool by null code should fail");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.TOOL0001, ex.getErrorCode());
        }
        try {
            toolService.getToolByCode("");
            fail("Get tool by empty code should fail");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.TOOL0001, ex.getErrorCode());
        }
        try {
            toolService.getToolByCode("     ");
            fail("Get tool by blank code should fail");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.TOOL0001, ex.getErrorCode());
        }
    }

    /**
     * Tool search should fail if tool code does not match any tools
     */
    @Test
    void getToolByCodeShouldFailIfCodeDoesNotMatchAnyTools() {
        try {
            ReflectionTestUtils.setField(toolService,"tools", new ArrayList<>());
            toolService.getToolByCode("LADR");
            fail("Tool search should fail when there are no tools");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.TOOL0002, ex.getErrorCode());
        }
        try {
            ReflectionTestUtils.setField(toolService,"tools", List.of(
                    new Tool("LADW", "Ladder", "Werner")
            ));
            toolService.getToolByCode("LADR");
            fail("Tool search should fail when there are no matching tools");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.TOOL0002, ex.getErrorCode());
        }
    }

    /**
     * Tool code should fail if tool does not have a matching tool type
     */
    @Test
    void getToolByCodeShouldFailIfToolDoesNotHaveAMatchingToolType() {
        try {
            ReflectionTestUtils.setField(toolService,"tools", List.of(
                    new Tool("LADW", "Ladder", "Werner")
            ));
            ReflectionTestUtils.setField(toolService,"toolTypes", new ArrayList<>());
            toolService.getToolByCode("LADW");
            fail("Tool search should fail when there are no matching tool types");
        } catch (ToolManagementException ex) {
            assertEquals(ErrorCode.TOOL0003, ex.getErrorCode());
        }
    }

    /**
     * When tool code is correct and has matching tool type it should work
     */
    @Test
    void getToolByCodeShouldWorkWhenThereIsAMatchingToolAndToolType() {
        ToolInformation expected = new ToolInformation(
                "LADW", "Werner", new ToolType(
                    "Ladder", BigDecimal.valueOf(1), true, false, false
        ));
        try {
            ReflectionTestUtils.setField(toolService,"tools", List.of(
                    new Tool("LADW", "Ladder", "Werner")
            ));
            ReflectionTestUtils.setField(toolService,"toolTypes", List.of(
                    new ToolType("Ladder", BigDecimal.valueOf(1), true, false, false)
            ));
            ToolInformation actual = toolService.getToolByCode("LADW");
            assertTrue(expected.equals(actual));
        } catch (ToolManagementException ex) {
            fail(ex.getMessage());
        }
    }
}