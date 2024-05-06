Jedná se o backend systému na vytváření rezervací termínů u zubaře.
Tento backend je realizován pomocí ve spring bootu jako rest api, localhost konfigurace běží na endpointu http://localhost:8080/api/v1/.
Backend disponuje endpointy pro registraci uživatelů, přihlášení uživatelů, vytváření rezervací uživatelů, rušení termínů uživatelů, zobrazení termínů uživatelů, zobrazení volných termínů.
Endpointy hlídají chybné requesty a odpovídají na ně případnými chybami s popisem, co se nepovedlo, tato odpověď je ve formě stavového kódu + textu.