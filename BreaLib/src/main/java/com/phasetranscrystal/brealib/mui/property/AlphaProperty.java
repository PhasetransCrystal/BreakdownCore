package com.phasetranscrystal.brealib.mui.property;

import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.view.View;

public class AlphaProperty extends FloatProperty<View> {

    public static final AlphaProperty INSTANCE = new AlphaProperty();

    public AlphaProperty() {
        super("hover_alpha");
    }

    @Override
    public void setValue(View object, float value) {
        object.setAlpha(value);
    }

    @Override
    public Float get(View object) {
        return object.getAlpha();
    }
}
