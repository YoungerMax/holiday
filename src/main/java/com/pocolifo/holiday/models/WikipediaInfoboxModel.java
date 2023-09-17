package com.pocolifo.holiday.models;

import java.util.*;

public class WikipediaInfoboxModel {
    public String title;
    public String subheader;
    public List<String> imageUrls = new ArrayList<>();
    public Map<String, List<String>> data = new LinkedHashMap<>();
}
