package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: lyy
 */

@ApiModel
@Data
public class SearchMeetingByIdFrom {
    @NotNull
    @Min(1)
    private Integer id;
}