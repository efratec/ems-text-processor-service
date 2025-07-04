package com.algaworks.algapost.api.model;

import java.math.BigDecimal;
import java.util.UUID;

public record PostResult(UUID postId, Integer wordCount, BigDecimal calculatedValue) {
}
