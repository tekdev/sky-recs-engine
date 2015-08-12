package com.sky.assignment.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "recommendation1")
public class Recommendation {

    @XmlElement
    public final String uuid;

    @XmlElement
    public final Long start;

    @XmlElement
    public final Long end;

    private Recommendation() {
        this(null, null, null);
    }

    public Recommendation(String uuid, Long start, Long end) {
        this.uuid = uuid;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("<recommendations>");
        result.append("<uuid>" + uuid +"</uuid>");
        result.append("<start>" + start +"</start>");
        result.append("<end>" + end +"</end>");
        result.append("</recommendations>");
        return result.toString();
    }
}
