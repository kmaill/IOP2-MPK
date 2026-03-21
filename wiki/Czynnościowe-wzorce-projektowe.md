# 1. Strategia
Co zostało zrobione:
- Utworzono interfejs `MapRenderStrategy` z metodą `render(MapElement element)`.
- Stworzono dwie implementacje strategii: `SchematicMapStrategy` (widok uproszczony) oraz `RealTimeMapStrategy` (widok z koordynatami GPS).
- Zmodyfikowano klasę `Map`, dodając pole `strategy` oraz metodę `setStrategy`.
- Metoda `render` w klasie `Map` deleguje zadanie wyświetlania do aktywnej strategii (lub wykonuje domyślne działanie, jeśli strategia nie jest ustawiona).

Dlaczego:
- Cel: Umożliwienie dynamicznej zmiany sposobu prezentacji mapy (np. przełączanie między widokiem schematycznym a rzeczywistym) bez modyfikacji samej klasy `Map`.
- Korzyść: Klasa `Map` nie musi zawierać rozbudowanych instrukcji warunkowych (`if-else` lub `switch`) sprawdzających tryb wyświetlania. Nowe sposoby renderowania można dodawać jako nowe klasy implementujące interfejs.

# 2. Obserwator
Co zostało zrobione:
- Utworzono interfejsy `Observer` (z metodą `update`) oraz `Subject` (z metodami `attach`, `detach`, `notifyObservers`).
- Klasa `BusLine` implementuje `Subject` – zarządza listą subskrybentów (pasażerów) i powiadamia ich o zmianach (np. opóźnieniach).
- Klasa `Passenger` implementuje `Observer` – odbiera powiadomienia i reaguje na nie (np. wyświetlając komunikat).

Dlaczego:
- Cel: Automatyczne powiadamianie zainteresowanych użytkowników (pasażerów) o zdarzeniach dotyczących konkretnej linii autobusowej.
- Korzyść: Luźne powiązanie między źródłem zdarzeń (`BusLine`) a odbiorcami (`Passenger`). Linia autobusowa nie musi wiedzieć, kim są odbiorcy ani ile ich jest; po prostu wysyła sygnał. Ułatwia to skalowanie systemu powiadomień.

# 3. Iterator
Co zostało zrobione:
- Klasa `Route` zaimplementowała interfejs `Iterable<BusStop>`.
- Dzięki temu klasa ta zwraca iterator, który pozwala na sekwencyjny dostęp do listy przystanków (`busStops`).

Dlaczego:
- Cel: Umożliwienie standardowego przeglądania elementów kolekcji (przystanków w trasie) bez ujawniania wewnętrznej reprezentacji tej kolekcji (np. czy jest to `ArrayList`, `LinkedList` czy tablica).
- Korzyść: Klient (np. kod wyświetlający trasę) może używać pętli `for-each` (np. `for (BusStop stop : route)`) w sposób czytelny i bezpieczny. Zmiana wewnętrznej struktury przechowywania przystanków w klasie `Route` nie wpłynie na kod klienta korzystającego z iteratora.