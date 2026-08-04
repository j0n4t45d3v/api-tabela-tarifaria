package com.jonatas.apitabelatarifaria.dto;

import java.math.BigDecimal;

public record FaixaConsumoRequest(Integer de, Integer ate, BigDecimal valorUnitario) {

}
