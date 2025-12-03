package com.phasetranscrystal.breacore.api.item.component;

import com.phasetranscrystal.breacore.api.item.IComponentItem;

import net.minecraft.world.item.Item;

/**
 * 描述可附加到{@link IComponentItem}的通用组件
 * 一个项目可以连接多个组件
 */
public interface IItemComponent {

    default void onAttached(Item item) {}
}
