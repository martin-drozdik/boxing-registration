package com.boxingregistration.app.domain

data class YearCategory
(
    val year: String,
    val categories: List<String>
)

fun getAllYearCategories(): List<YearCategory>
{
    return listOf(
    YearCategory("2008-2006 Mladší žiaci",  listOf("30","32","35","38","40","42","44","46","48","50","52","54","57","60","63","66","70","+75")),
    YearCategory("2008-2006 Mladšie žiačky",  listOf("30","32","35","38","40","42","44","46","48","50","52","54","57","60","63","66","70","+75")),
    YearCategory("2004-2005 Starší žiaci",  listOf("38.5","40","41","5","43","44","5","46","48","50","52","54","56","59","62","65","68","72","76","+76")),
    YearCategory("2004-2005 Staršie žiačky",  listOf("38.5","40","41","5","43","44","5","46","48","50","52","54","56","59","62","65","68","72","76","+76")),
    YearCategory("2003-2002 Mladší dorastenci",  listOf("46","48","50","52","54","57","60","63","66","70","75","80","+80")),
    YearCategory("2003-2002 Mladšie dorastenky",  listOf("46","48","50","52","54","57","60","63","66","70","75","80","+80")),
    YearCategory("2001-2000 Starší dorastenci",  listOf("49","52","56","60","64","69","75","81","91","+91")),
    YearCategory("2001-2000 Staršie dorastenky",  listOf("48","51","54","57","60","64","69","75","81","+81")),
    YearCategory("1999 – 1978 Muži",  listOf("49","52","56","60","64","69","75","81","91","+91")),
    YearCategory("1999 – 1978 Ženy",  listOf("48","51","54","57","60","64","69","75","81","+81")))
}