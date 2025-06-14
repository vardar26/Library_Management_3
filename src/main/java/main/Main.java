package main;

import entity.*;
import dao.*;
import util.HibernateUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookDAO bookDAO = new BookDAO();
        BookBorrowingDAO borrowingDAO = new BookBorrowingDAO();

        while (true) {
            System.out.println("\n1- Kitap Ekle");
            System.out.println("2- Kitapları Listele");
            System.out.println("3- Kitap Ödünç Al");
            System.out.println("4- Kitap İade Et");
            System.out.println("5- Çıkış");
            System.out.print("Seçiminiz: ");
            int secim = Integer.parseInt(sc.nextLine());

            if (secim == 1) {
                System.out.print("Kitap adı: ");
                String name = sc.nextLine();
                System.out.print("Basım yılı: ");
                int year = Integer.parseInt(sc.nextLine());
                System.out.print("Stok: ");
                int stock = Integer.parseInt(sc.nextLine());
                // Basitlik için ilk author/publisher'ı çekelim (gelişmiş projede menüden seçtirirsin)
                Author author = new AuthorDAO().getAllAuthors().get(0);
                Publisher publisher = new PublisherDAO().getAllPublishers().get(0);
                List<Category> categories = new CategoryDAO().getAllCategories();
                Book book = new Book(name, year, stock);
                book.setAuthor(author);
                book.setPublisher(publisher);
                book.setCategories(categories);
                bookDAO.saveBook(book);
                System.out.println("Kitap eklendi.");
            } else if (secim == 2) {
                System.out.println("Mevcut Kitaplar:");
                for (Book b : bookDAO.getAllBooks()) {
                    System.out.println(b.getId() + " - " + b.getName() + " (Stok: " + b.getStock() + ")");
                }
            } else if (secim == 3) {
                System.out.print("Ödünç alınacak kitap ID: ");
                Long bookId = Long.parseLong(sc.nextLine());
                Book book = bookDAO.getBookById(bookId);
                if (book != null && book.getStock() > 0) {
                    System.out.print("Ödünç alan kişi: ");
                    String borrower = sc.nextLine();
                    BookBorrowing borrowing = new BookBorrowing(borrower, LocalDate.now());
                    borrowing.setBook(book);
                    book.setStock(book.getStock() - 1);
                    borrowingDAO.saveBookBorrowing(borrowing);
                    bookDAO.updateBook(book);
                    System.out.println("Ödünç verildi.");
                } else {
                    System.out.println("Kitap bulunamadı ya da stok yok.");
                }
            } else if (secim == 4) {
                System.out.print("İade edilecek ödünç ID: ");
                Long borrowId = Long.parseLong(sc.nextLine());
                BookBorrowing borrowing = borrowingDAO.getBookBorrowingById(borrowId);
                if (borrowing != null && borrowing.getReturnDate() == null) {
                    borrowing.setReturnDate(LocalDate.now());
                    Book book = borrowing.getBook();
                    book.setStock(book.getStock() + 1);
                    borrowingDAO.updateBookBorrowing(borrowing);
                    bookDAO.updateBook(book);
                    System.out.println("Kitap iade edildi.");
                } else {
                    System.out.println("Kayıt bulunamadı veya zaten iade edilmiş.");
                }
            } else if (secim == 5) {
                break;
            }
        }
        sc.close();
        HibernateUtil.shutdown();
        System.out.println("Program bitti.");
    }

}



    /*
   public static void main(String[] args) {
        // 1. Author, publisher ve kategorileri DB'ye kaydet
        Author author = new Author("Sabahattin Ali", 1907, "Türkiye");
        new AuthorDAO().saveAuthor(author);
        author = new AuthorDAO().getAuthorById(author.getId());

        Publisher publisher = new Publisher("YKY", 1945, "İstanbul");
        new PublisherDAO().savePublisher(publisher);
        publisher = new PublisherDAO().getPublisherById(publisher.getId());

        Category roman = new Category("Roman", "Kurgu romanları");
        new CategoryDAO().saveCategory(roman);
        roman = new CategoryDAO().getCategoryById(roman.getId());

        Category deneme = new Category("Deneme", "Deneme kitapları");
        new CategoryDAO().saveCategory(deneme);
        deneme = new CategoryDAO().getCategoryById(deneme.getId());

// 2. Book'u oluşturup ilişkileri bağla
        Book book = new Book("Kürk Mantolu Madonna", 1943, 10);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setCategories(Arrays.asList(roman, deneme));


        new BookDAO().saveBook(book);
        if (!new BookDAO().existsByName("Kürk Mantolu Madonna")) {
            new BookDAO().saveBook(book);
        } else {
            System.out.println("Bu kitap zaten kayıtlı!");
        }


// 3. BookBorrowing ile işlem yap
        BookBorrowing borrowing = new BookBorrowing("Ahmet Yılmaz", LocalDate.now());
        borrowing.setBook(book);
        new BookBorrowingDAO().saveBookBorrowing(borrowing);

// 4. Kitap iade etme (teslim tarihi güncelleme)
        borrowing.setReturnDate(LocalDate.now().plusDays(7)); // 7 gün sonra teslim
        new BookBorrowingDAO().updateBookBorrowing(borrowing);

// 5. Tüm kitapları listele
        System.out.println("Mevcut Kitaplar:");
        for (Book b : new BookDAO().getAllBooks()) {
            System.out.println(b.getId() + " - " + b.getName());
        }

// 6. Kapanış
        HibernateUtil.shutdown();
        System.out.println("Program bitti.");

    }
}
*/
