package com.foodie.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain=true)

public class ShopSearchPrompt {
    private String shopName;
    private String shopNotice;
}
