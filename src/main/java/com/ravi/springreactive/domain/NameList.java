package com.ravi.springreactive.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NameList {
    private List<String> names = new ArrayList<>();
}
