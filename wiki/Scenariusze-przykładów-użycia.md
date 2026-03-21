# 1. Sprawdzenie rozkładu jazdy dla danego przystanku:
# Warunki początkowe:
- Pasażer musi mieć uruchomioną aplikację
- Aplikacja prezentuje okno główne
# Warunki końcowe:
- Pasażer sprawdził rozkład jazdy
- Wybrana przez pasażera linia jest niedostępna
# Aktorzy: Pasażer
# Przepływ zdarzeń:
1. Pasażer wybiera opcję sprawdzenia rozkładu jazdy dla przystanku.
2. Aplikacja wyświetla dostępne linie autobusowe dla danego przystanku.
3. Pasażer wybiera interesującą go linię.
4. Aplikacja wyświetla rozkład jazdy dla wybranej linii autobusowej.
# Alternatywny przepływ zdarzeń:
- 3a. Powiadomienie o tym, że interesująca linia jest niedostępna, użytkownik potwierdził, że otrzymał o tym informację POWRÓT DO 2.

# 2. Sprawdzanie lokalizacji autobusu i przystanków:
# Warunki początkowe:
- Pasażer ma uruchomioną aplikację.
- Aplikacja prezentuje okno główne.
# Warunki końcowe:
- Pasażer sprawdził lokalizację wybranego autobusu lub informacje o odjazdach z wybranego przystanku.
# Aktorzy:
- Pasażer
# Przepływ zdarzeń:
1. Pasażer wybiera opcję wyświetlenia mapy.
2. Aplikacja wyświetla mapę z lokalizacją pasażera oraz znacznikami aktywnych autobusów i przystanków.
3. Pasażer wybiera interesujący go znacznik autobusu na mapie.
4. Aplikacja wyświetla szczegółowe informacje o autobusie: numer linii, trasę, aktualny stan (np. opóźnienie) oraz przewidywany czas przyjazdu na kolejne przystanki.
# Alternatywny przepływ zdarzeń:
- 3a. Pasażer wybiera interesujący go znacznik przystanku na mapie.
- 4a. Aplikacja wyświetla nazwę przystanku oraz tabelę najbliższych odjazdów (planowych i rzeczywistych) dla wszystkich linii zatrzymujących się na tym przystanku.

# 3. Otrzymywanie powiadomień o opóźnieniach i zmianach w kursowaniu:
# Warunki początkowe:
- Pasażer ma zainstalowaną aplikację z włączonymi powiadomieniami.
- System monitoruje autobusy.
# Warunki końcowe:
- Pasażer został poinformowany o istotnej zmianie dotyczącej kursu.
# Aktorzy:
- Pasażer, 
- System
# Przepływ zdarzeń:
1. System wykrywa znaczące opóźnienie.
2. System automatycznie wysyła powiadomienie do użytkowników, którzy np. obserwują daną linię.
3. Pasażer otrzymuje powiadomienie na swoim urządzeniu.
4. Pasażer otwiera powiadomienie, a aplikacja wyświetla szczegóły zmiany czasu dojazdu autobusu na dany przystanek.
# Alternatywny przepływ zdarzeń:
- 3a. Pasażer ignoruje lub odrzuca powiadomienie. KONIEC

# 4. Aktualizowanie rozkładu jazdy:
# Warunki początkowe:
- Administrator jest zalogowany do panelu administracyjnego systemu.
# Warunki końcowe:
- Rozkład jazdy dla wybranej linii został zmodyfikowany, dodany lub usunięty.
- Nowy rozkład jest widoczny dla pasażerów.
# Aktorzy: 
- Administrator
# Przepływ zdarzeń:
1. Administrator wybiera opcję zarządzania rozkładami jazdy.
2. System wyświetla interfejs do zarządzania liniami i ich rozkładami.
3. Administrator wybiera linię do modyfikacji lub tworzy nową.
4. Administrator wprowadza zmiany w godzinach kursowania, trasie lub dniach obowiązywania rozkładu.
5. Administrator zapisuje zmiany.
6. System weryfikuje poprawność danych i aktualizuje rozkład jazdy w całej aplikacji oraz wysyła powiadomienie do użytkowników o zmianie.
# Alternatywny przepływ zdarzeń:
- 5a. System wykrywa błąd w wprowadzonych danych (np. nieprawidłowy format czasu). System wyświetla komunikat o błędzie. Administrator koryguje dane, POWRÓT do 5.