package me.centralworks.bungee.modules.reports.models;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Report {

    private Integer id = 0;
    private String victim;
    private String reason = "";
    private Long date = System.currentTimeMillis();
    private List<String> evidences = Lists.newArrayList();

}
