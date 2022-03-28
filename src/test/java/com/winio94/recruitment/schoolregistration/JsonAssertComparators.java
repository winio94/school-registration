package com.winio94.recruitment.schoolregistration;

import org.json.JSONObject;
import org.skyscreamer.jsonassert.ValueMatcher;

public class JsonAssertComparators {

    public static final ValueMatcher<Object> NON_NULL_VALUE_MATCHER = (o1, o2) -> (o1 != null && !o1.equals(
        JSONObject.NULL)) && (o2 != null && !o2.equals(JSONObject.NULL));
}
