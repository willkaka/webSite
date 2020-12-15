package com.hyw.webSite.dto;

import lombok.Data;

import java.util.List;

@Data
public class GitFileChangeInfoDto {

    private String fileName;
    private List<GitCommitInfoDto> gitCommitInfoDtoList;
}
