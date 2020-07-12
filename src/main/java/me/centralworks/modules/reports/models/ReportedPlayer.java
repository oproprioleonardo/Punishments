package me.centralworks.modules.reports.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ReportedPlayer {

    private String user;
    private List<Integer> data;

}
