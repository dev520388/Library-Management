# 📚 Library Management System - A Java Project

A simple yet powerful Library Management System built using Java. This project streamlines the core library operations like searching, borrowing, and returning books with a focus on clean object-oriented design and database integration.

---

## 🚀 Features

- 🔍 **Book Search**: Find books by title or author.
- 📥 **Borrow Books**: Issue books to users while updating inventory.
- 📤 **Return Books**: Return and restock books efficiently.
- 📚 **Book Classification**: Automatic category classification using inheritance.
- 💾 **Database Integration**: Store book info, user profiles, and transactions.
- 🖥️ **User-Friendly Interface**: With search bar, book details, and action buttons.

---

## 🧠 Project Structure

### 📦 Class Hierarchy
- **Base Class**: `Book`  
  Common attributes: `title`, `author`, `ISBN`.

- **Derived Classes**:
  - `Textbook`: Adds `course` and `subject`.
  - `Novel`: Adds `genre` and `targetAudience`.

### 📂 Key Functionalities
| Function | Description |
|---------|-------------|
| `searchBook()` | Queries the database for matching books. |
| `borrowBook()` | Checks availability and issues the book. |
| `returnBook()` | Updates inventory and user history. |

---

## 🗃️ Database Schema

- **Book Records**: Title, Author, ISBN, Category, Availability
- **Users**: Profile info and borrowing history
- **Transactions**: Borrowing/return timestamps and book-user links

---

## 🧑‍💻 How to Run the Project

### 🔧 Requirements

- Java 8+
- MySQL or any SQL-compliant DB
- JDBC Driver
- IDE like IntelliJ IDEA or Eclipse

### 📥 Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/library-management-system-java.git
   cd library-management-system-java
