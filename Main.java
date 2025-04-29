import java.util.*;

class Movie {
    private String title;
    private int duration;
    private String genre;

    public Movie(String title, int duration, String genre) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return title + " (" + genre + ", " + duration + " mins)";
    }
}

class Seat {
    private int seatNumber;
    private boolean isAvailable;

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.isAvailable = true;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void book() {
        isAvailable = false;
    }

    public void release() {
        isAvailable = true;
    }

    public String toString() {
        return "Seat " + seatNumber + " - " + (isAvailable ? "Available" : "Booked");
    }
}

class Screen {
    private int screenNumber;
    private List<Seat> seats;

    public Screen(int screenNumber, int totalSeats) {
        this.screenNumber = screenNumber;
        this.seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new Seat(i));
        }
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getScreenNumber() {
        return screenNumber;
    }
}

class Show {
    private Movie movie;
    private Screen screen;
    private String time;

    public Show(Movie movie, Screen screen, String time) {
        this.movie = movie;
        this.screen = screen;
        this.time = time;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public String getTime() {
        return time;
    }

    public String toString() {
        return movie.getTitle() + " at " + time + " on Screen " + screen.getScreenNumber();
    }
}

class Booking {
    private Show show;
    private List<Seat> seats;

    public Booking(Show show, List<Seat> seats) {
        this.show = show;
        this.seats = seats;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Booking for " + show + "\nSeats: ");
        for (Seat s : seats) {
            sb.append(s.getSeatNumber()).append(" ");
        }
        return sb.toString();
    }
}

class User {
    private String username;
    private String password;
    private List<Booking> bookings;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.bookings = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addBooking(Booking b) {
        bookings.add(b);
    }

    public void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }
    }
}

class UserManager {
    private List<User> users = new ArrayList<>();

    public User register(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                System.out.println("Username already exists.");
                return null;
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("User registered successfully.");
        return newUser;
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.checkPassword(password)) {
                System.out.println("Login successful!");
                return u;
            }
        }
        System.out.println("Invalid credentials.");
        return null;
    }
}

public class Main {
    private static List<Movie> movies = new ArrayList<>();
    private static List<Show> shows = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();

    public static void main(String[] args) {
        setupData();

        User currentUser = null;
        while (currentUser == null) {
            System.out.println("\n1. Register\n2. Login\nChoose: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (choice == 1) {
                currentUser = userManager.register(username, password);
            } else if (choice == 2) {
                currentUser = userManager.login(username, password);
            }
        }

        while (true) {
            System.out.println("\n1. View Shows\n2. Book Ticket\n3. View My Bookings\n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    listShows();
                    break;
                case 2:
                    bookTicket(currentUser);
                    break;
                case 3:
                    currentUser.viewBookings();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private static void listShows() {
        System.out.println("\nAvailable Shows:");
        for (int i = 0; i < shows.size(); i++) {
            System.out.println((i + 1) + ". " + shows.get(i));
        }
    }

    private static void bookTicket(User user) {
        listShows();
        System.out.print("Choose a show: ");
        int showIndex = scanner.nextInt() - 1;
        if (showIndex < 0 || showIndex >= shows.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Show selectedShow = shows.get(showIndex);
        List<Seat> seats = selectedShow.getScreen().getSeats();

        System.out.println("\nAvailable Seats:");
        for (Seat seat : seats) {
            System.out.println(seat);
        }

        System.out.print("Enter seat numbers (comma separated): ");
        scanner.nextLine();
        String[] input = scanner.nextLine().split(",");

        List<Seat> selectedSeats = new ArrayList<>();
        for (String s : input) {
            int seatNum = Integer.parseInt(s.trim());
            if (seatNum < 1 || seatNum > seats.size()) {
                System.out.println("Invalid seat: " + seatNum);
                continue;
            }

            Seat seat = seats.get(seatNum - 1);
            if (seat.isAvailable()) {
                seat.book();
                selectedSeats.add(seat);
            } else {
                System.out.println("Seat " + seatNum + " already booked.");
            }
        }

        if (!selectedSeats.isEmpty()) {
            Booking booking = new Booking(selectedShow, selectedSeats);
            user.addBooking(booking);
            System.out.println("Booking Confirmed:\n" + booking);
        } else {
            System.out.println("No seats booked.");
        }
    }

    private static void setupData() {
        Movie m1 = new Movie("Inception", 148, "Sci-Fi");
        Movie m2 = new Movie("Titanic", 195, "Romance");
        movies.add(m1);
        movies.add(m2);

        Screen s1 = new Screen(1, 10);
        Screen s2 = new Screen(2, 10);

        shows.add(new Show(m1, s1, "5:00 PM"));
        shows.add(new Show(m2, s2, "8:00 PM"));
    }
}
