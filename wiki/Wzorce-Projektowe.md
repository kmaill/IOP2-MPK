# 1. Metoda Fabrykująca
Co zostało zrobione:
- Utworzono nową klasę `UserFactory` w pakiecie `org.mpk`.
- Zaimplementowano metodę `createUser(String type, int id, String username)`, która przyjmuje typ użytkownika (jako String) oraz dane inicjalizacyjne.
- Zmodyfikowano klasę abstrakcyjną `User` oraz jej klasy pochodne `Administrator` i `Passenger`, dodając do nich konstruktory przyjmujące `id` oraz `username`. Wcześniej klasy te polegały na domyślnych konstruktorach.

Dlaczego:
- Cel: Separacja logiki tworzenia obiektów od ich użycia.
- Korzyść: Klient (np. klasa `SystemMpk` lub moduł logowania) nie musi wiedzieć, jak dokładnie tworzy się `Administratora` czy `Pasażera`. Wystarczy, że poprosi fabrykę o instancję. Ułatwia to w przyszłości dodanie nowych ról (np. `Kierowca`) bez zmieniania kodu w wielu miejscach w systemie, a jedynie w fabryce.

# 2. Budowniczy
Co zostało zrobione:
- Zmodyfikowano klasę `Route`.
- Główny konstruktor klasy `Route` został zmieniony na prywatny, aby uniemożliwić bezpośrednie tworzenie obiektów z pominięciem Budowniczego.
- Dodano statyczną klasę wewnętrzną `Builder`.
- W klasie `Builder` zaimplementowano metody ustawiające pola (`id`, `name`) oraz metodę `addStop(BusStop stop)`, która inicjalizuje listę przystanków i pozwala na dodawanie ich pojedynczo.
- Dodano metodę `build()`, która finalizuje proces i zwraca gotowy obiekt `Route`.

Dlaczego:
- Cel: Ułatwienie konstrukcji złożonych obiektów.
- Korzyść: Wcześniej, aby stworzyć trasę, trzeba było najpierw ręcznie utworzyć listę `List<BusStop>`, dodać do niej elementy, a potem przekazać całą listę do konstruktora. Teraz kod jest znacznie czytelniejszy i bardziej "płynny" (fluent interface), np. `.addStop(s1).addStop(s2)`.

# 3. Prototyp
Co zostało zrobione:
- Zmodyfikowano klasę `Schedule`.
- Klasa ta teraz implementuje interfejs `Cloneable`.
- Nadpisano metodę `clone()`, wykorzystując mechanizm `super.clone()` (kopia, wystarczająca dla pól typów prostych i niezmiennych jak `LocalDate` czy `String`).

Dlaczego:
- Cel: Umożliwienie tworzenia nowych obiektów na bazie już istniejących (klonowanie).
- Korzyść: W systemach rozkładów jazdy często zdarza się, że rozkład na wtorek jest taki sam jak na poniedziałek. Zamiast tworzyć nowy obiekt `Schedule` od zera i ustawiać te same parametry, system może sklonować istniejący rozkład i zmienić w nim tylko datę obowiązywania. Jest to szybsze i mniej podatne na błędy przy przepisywaniu danych.

# 4. Singleton
Co zostało zrobione:
- W klasie `SystemMpk` zastosowano prywatny konstruktor, uniemożliwiający tworzenie obiektów tej klasy z zewnątrz.
- Utworzono prywatne statyczne pole `instance` przechowujące jedyną instancję klasy.
- Udostępniono publiczną metodę statyczną `getInstance()`, która zwraca instancję klasy, tworząc ją tylko przy pierwszym wywołaniu.

Dlaczego:
- Cel: Zagwarantowanie istnienia dokładnie jednej instancji głównego kontrolera systemu.
- Korzyść: Klasa `SystemMpk` odpowiada za kluczowe operacje globalne (np. uwierzytelnianie). Posiadanie wielu instancji mogłoby doprowadzić do niespójności stanu aplikacji (np. różnych wizji monitoringu w różnych obiektach). Singleton zapewnia jeden, spójny punkt prawdy i dostępu do usług systemowych.
