package entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "barrowings")

public class BookBorrowing {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private Long id;
    private String borrowerName;

    private LocalDate borrowingDate;

    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    // Constructors
    public BookBorrowing() {}

    public BookBorrowing(String borrowerName, LocalDate borrowingDate) {
        this.borrowerName = borrowerName;
        this.borrowingDate = borrowingDate;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }
    public LocalDate getBorrowingDate() { return borrowingDate; }
    public void setBorrowingDate(LocalDate borrowingDate) { this.borrowingDate = borrowingDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

}
