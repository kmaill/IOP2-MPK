# 1. Kompozyt
Co zostało zrobione:
- Utworzono nowy interfejs `MapElement` z metodą `display()`.
- Zmodyfikowano klasy `BusStop`, `Bus` oraz `Route` tak, aby implementowały ten interfejs.
- Klasa `Route` pełni rolę kompozytu – przechowuje listę przystanków (`BusStop`) i w swojej metodzie `display()` wywołuje metody `display()` dla każdego z nich.
- Klasa `BusStop` i `Bus` pełnią rolę elementów podstawowych.
- Zastąpiono wiele specyficznych metod w klasie `Map` (np. `displayBusStops`, `displayRoute`) jedną uniwersalną metodą `render(MapElement element)`.

Dlaczego:
- Cel: Umożliwienie klientowi (klasie `Map`) traktowania pojedynczych obiektów i grup obiektów w jednolity sposób.
- Korzyść: Klasa `Map` stała się prostsza i bardziej elastyczna. Jeśli w przyszłości dojdą nowe elementy mapy, wystarczy, że będą implementować `MapElement`, a klasa `Map` obsłuży je bez konieczności modyfikacji kodu.

# 2. Fasada
Co zostało zrobione:
- Wydzielono logikę z klasy `SystemMpk` do dedykowanych klas: `TrafficService`, `NotificationService` oraz `AuthenticationService`.
- Klasa `SystemMpk` została przekształcona w fasadę. W jej konstruktorze inicjalizowane są obiekty poszczególnych klas.
- Metody publiczne klasy `SystemMpk` (np. `monitorTraffic`, `authenticateUser`) nie realizują już logiki samodzielnie, lecz delegują wywołania do odpowiednich metod w serwisach podrzędnych.

Dlaczego:
- Cel: Dostarczenie prostego, ujednoliconego interfejsu do zbioru bardziej skomplikowanych podsystemów.
- Korzyść: Użytkownik systemu nie musi znać szczegółów implementacyjnych poszczególnych serwisów ani zarządzać ich zależnościami. Komunikuje się tylko z `SystemMpk`. Pozwala to również na odseparowanie logiki zgodnie z zasadą pojedynczej odpowiedzialności, jednocześnie zachowując jeden punkt dostępu dla aplikacji.

# 3. Proxy
Co zostało zrobione:
- Utworzono interfejs `AdminInterface`, definiujący metody administracyjne (np. `manageSchedules`).
- Klasa `Administrator` implementuje ten interfejs.
- Utworzono klasę `AdministratorProxy` implementującą `AdminInterface`. Przechowuje ona referencję do obiektu `Administrator`.
- W metodzie `manageSchedules` w Proxy dodano logikę sprawdzającą, czy administrator jest zalogowany (`isLoggedIn()`), zanim wywołanie zostanie przekazane do rzeczywistego obiektu.

Dlaczego:
- Cel: Kontrola dostępu do metod wrażliwych.
- Korzyść: Zapewnienie bezpieczeństwa operacji. Użytkownik, który posiada obiekt `Administrator`, ale nie dokonał logowania, nie będzie mógł wykonać operacji administracyjnych. Logika autoryzacji jest oddzielona od samej logiki zarządzania rozkładami.