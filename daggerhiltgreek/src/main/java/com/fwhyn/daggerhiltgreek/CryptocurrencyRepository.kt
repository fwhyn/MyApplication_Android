package com.fwhyn.daggerhiltgreek

interface CryptocurrencyRepository {
    fun getCryptoCurrency(): List<Cryptocurrency>
}