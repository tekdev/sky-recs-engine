package com.sky.assignment.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Recommendations {

    public final List<Recommendation> recommendations;

    private Recommendations() {
        this(null);
    }

    public Recommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public String toString() {
        if (this.recommendations != null) {
            StringBuilder result = new StringBuilder();
            result.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            result.append("<recommendations>");
            for (Recommendation r : recommendations) {
                result.append(r.toString());
            }
            result.append("</recommendations>");
            return result.toString();
        } else return "";
    }
}