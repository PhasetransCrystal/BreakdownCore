package com.phasetranscrystal.breacore.api.mui.editor;

import com.phasetranscrystal.brealib.mui.widget.Widget;
import com.phasetranscrystal.brealib.mui.widget.WidgetGroup;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface IEditableUI<W extends Widget, T> {

    W createDefault();

    void setupUI(WidgetGroup template, T instance);

    record Normal<A extends Widget, B>(Supplier<A> supplier, BiConsumer<WidgetGroup, B> binder)
            implements IEditableUI<A, B> {

        @Override
        public A createDefault() {
            return supplier.get();
        }

        @Override
        public void setupUI(WidgetGroup template, B instance) {
            binder.accept(template, instance);
        }
    }
}
