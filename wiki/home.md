# System rozkładu jazdy MZK — opis działania
# 1. Działanie systemu
System monitoruje położenie autobusu i udostępnia dane w czasie rzeczywistym oraz prezentuje rozkład jazdy. 

Główne funkcje:
Sprawdzanie lokalizacji autobusu (GPS) — aktualna pozycja pojazdu.
Wyświetlanie rzeczywistego oraz ustalonego (planowego) czasu przyjazdu na przystanek.
Powiadomienia o opóźnieniach, odwołaniach i zmianach trasy.

# Mapa
Mapa jest:

Schematyczna — uproszczona grafika tras i przystanków (do wydruków lub szybkiego podglądu).
Rzeczywista — mapa z nałożonymi trasami i pozycjami autobusów.

# Autobus
Informacje dostępne dla kazdego autobusu:

Trasa: numer i przystanki autobusowe.
Lokalizacja: znacznik na mapie (na aktualnych przystankach).
Stan: opóźnienie względem rozkładu, przewidywany czas przejazdu.
Godziny kursowania (harmonogram) — dni robocze, weekendy, święta.

# Trasa
Atrybuty dla trasy:

Numer linii.
Szacowany czas przejazdu całej trasy oraz odcinków między przystankami.
Przystanki dla danej linii

# Przystanek
Dane przy przystanku:

Nazwa przystanku i identyfikator.
Godziny przyjazdu dla obsługiwanych linii (planowe i rzeczywiste).
Możliwość sprawdzenia najbliższych odjazdów oraz połączeń z innymi liniami.

# 2. Aktualizacje
Aktualizacje danych obejmują:

Automatyczne aktualizacje pozycji autobusów.
Ręczne lub zintegrowane aktualizacje rozkładu przez administratorów (np. zmiany rozkładu, wyłączenia odcinków).

# 3. Dostęp i uprawnienia
Podstawowe zasady dostępu:

Dostęp do odczytu rozkładu ma każdy.
Aktualizować rozkład może tylko administrator.

# 4. Cel systemu
Głównym celem jest umożliwienie pasażerom sprawnego i przewidywalnego przemieszczania się komunikacją miejską — dotarcie z punktu A do punktu B z informacją o czasie przejazdu.

# 5. User Stories
Jako administrator aktualizuję rozkład jazdy autobusów, aby był aktualny.
Jako pasażer, chcę otrzymywać informacje o opóźnieniach i rzeczywistym czasie przyjazdu autobusu, abym mógł efektywnie zarządzać swoim czasem i w razie potrzeby dostosować plany podróży.
Jako pasażer, chcę mieć możliwość sprawdzenia, jakie linie autobusowe odjeżdżają z danego przystanku, abym mógł zweryfikować, czy jest on dla mnie odpowiednim punktem początkowym w mojej podróży.
Jako pasażer chce wiedziec na jakie przystanki jedzie dany autobus, abym wiedział gdzie moge się nim dostać.
Jako pasażer chce znac rozklad jazdy autobusu, abym się na niego nie spóźnił.
Jako pasażer, chcę mieć możliwość śledzenia lokalizacji autobusu na mapie w czasie rzeczywistym, abym mógł dokładniej oszacować czas jego przyjazdu i zminimalizować niepewność oraz czas oczekiwania na przystanku.
# 6. Diagram
![diagram](uploads/2a427dfef40c795e9d1705e945aac40c/diagram.png)