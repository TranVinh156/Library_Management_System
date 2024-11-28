drop database if exists lms_database;

create database if not exists lms_database;
use lms_database;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+07:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE TABLE IF NOT EXISTS `Books`
(
    `ISBN`                 BIGINT                            NOT NULL,
    `title`                varchar(1000)                     NOT NULL,
    `image_path`           varchar(1000)                     NOT NULL DEFAULT 'bookImage/default.png',
    `description`          text,
    `placeAt`              varchar(250),
    `preview`              varchar(250)                               default NULL,
    `quantity`             int                               NOT NULL DEFAULT 0,
    `number_lost_book`     int                               NOT NULL DEFAULT 0,
    `number_loaned_book`   int                               NOT NULL DEFAULT 0,
    `number_reserved_book` int                               NOT NULL DEFAULT 0,
    `rate`                 int                               not null default 5,
    `BookStatus`           ENUM ('AVAILABLE', 'UNAVAILABLE') NOT NULL DEFAULT 'AVAILABLE',
    `added_at_timestamp`   timestamp                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk PRIMARY KEY (ISBN)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `Authors`
(
    `author_ID`   int(8)       NOT NULL AUTO_INCREMENT,
    `author_name` varchar(100) NOT NULL,
    CONSTRAINT pk PRIMARY KEY (author_ID)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 10000000;

CREATE TABLE IF NOT EXISTS `Category`
(
    `category_ID`   int(8)        NOT NULL AUTO_INCREMENT,
    `category_name` varchar(1000) NOT NULL,
    CONSTRAINT pk PRIMARY KEY (category_ID)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 20000000;

CREATE TABLE IF NOT EXISTS `Books_Authors`
(
    `ISBN`      bigint NOT NULL,
    `author_ID` int(8) NOT NULL,
    CONSTRAINT pk PRIMARY KEY (ISBN, author_ID),
    CONSTRAINT FOREIGN KEY (ISBN) REFERENCES Books (ISBN) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (author_ID) REFERENCES Authors (author_ID) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `Books_Category`
(
    `ISBN`        bigint NOT NULL,
    `category_ID` int(8) NOT NULL,
    CONSTRAINT pk PRIMARY KEY (ISBN, category_ID),
    CONSTRAINT FOREIGN KEY (ISBN) REFERENCES Books (ISBN) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (category_ID) REFERENCES Category (category_ID) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `BookItem`
(
    `barcode`        int(8)                                           NOT NULL AUTO_INCREMENT,
    `ISBN`           bigint                                           NOT NULL,
    `BookItemStatus` ENUM ('AVAILABLE', 'RESERVED', 'LOANED', 'LOST') NOT NULL DEFAULT 'AVAILABLE',
    `note`           varchar(1000),
    CONSTRAINT pk PRIMARY KEY (barcode),
    CONSTRAINT FOREIGN KEY (ISBN) REFERENCES Books (ISBN) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 30000000;

CREATE TABLE IF NOT EXISTS `Members`
(
    `member_ID`  int(8)                           NOT NULL AUTO_INCREMENT,
    `image_path` varchar(1000)                    NOT NULL DEFAULT 'avatar/default.png',
    `first_name` varchar(50)                      NOT NULL,
    `last_name`  varchar(50)                      NOT NULL,
    `birth_date` DATE                             NOT NULL,
    `gender`     ENUM ('MALE', 'FEMALE', 'OTHER') NOT NULL,
    `email`      varchar(250)                     NOT NULL UNIQUE,
    `phone`      varchar(10)                      NOT NULL UNIQUE,
    `role`       ENUM ('ADMIN', 'NONE')           NOT NULL DEFAULT 'NONE',
    CONSTRAINT pk PRIMARY KEY (member_ID)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 40000000;

CREATE TABLE IF NOT EXISTS `Users`
(
    `user_ID`            int(8)                                           NOT NULL AUTO_INCREMENT,
    `username`           varchar(250),
    `password`           varchar(250),
    `AccountStatus`      ENUM ('ACTIVE', 'BLACKLISTED', 'CLOSED', 'NONE') NOT NULL,
    `member_ID`          int(8)                                           NOT NULL,
    `otp`                varchar(6)                                                DEFAULT NULL,
    `otp_expiry`         TIMESTAMP                                                 DEFAULT NULL,
    `added_at_timestamp` timestamp                                        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk PRIMARY KEY (user_ID),
    CONSTRAINT FOREIGN KEY (member_ID) REFERENCES Members (member_ID) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 50000000;

CREATE TABLE IF NOT EXISTS `Admins`
(
    `admin_ID`           int(8)    NOT NULL AUTO_INCREMENT,
    `username`           varchar(250),
    `password`           varchar(250),
    `member_ID`          int(8)    NOT NULL,
    `added_at_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk PRIMARY KEY (admin_ID),
    CONSTRAINT FOREIGN KEY (member_ID) REFERENCES Members (member_ID) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 55000000;

CREATE TABLE IF NOT EXISTS `BookReservation`
(
    `reservation_ID`        int(8)                                    NOT NULL AUTO_INCREMENT,
    `member_ID`             int(8)                                    NOT NULL,
    `barcode`               int(8)                                    NOT NULL,
    `creation_date`         DATE                                      NOT NULL,
    `due_date`              DATE                                      NOT NULL,
    `BookReservationStatus` ENUM ('WAITING', 'COMPLETED', 'CANCELED') NOT NULL DEFAULT 'WAITING',
    CONSTRAINT pk PRIMARY KEY (reservation_ID),
    CONSTRAINT FOREIGN KEY (barcode) REFERENCES BookItem (barcode) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (member_ID) REFERENCES Members (member_ID) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 60000000;

CREATE TABLE IF NOT EXISTS `BookIssue`
(
    `issue_ID`        int(8)                                NOT NULL AUTO_INCREMENT,
    `member_ID`       int(8)                                NOT NULL,
    `barcode`         int(8)                                NOT NULL,
    `creation_date`   DATE                                  NOT NULL,
    `due_date`        DATE                                  NOT NULL,
    `return_date`     DATE,
    `BookIssueStatus` ENUM ('BORROWED', 'RETURNED', 'LOST') NOT NULL DEFAULT 'BORROWED',
    CONSTRAINT pk PRIMARY KEY (issue_ID),
    CONSTRAINT FOREIGN KEY (barcode) REFERENCES BookItem (barcode) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (member_ID) REFERENCES Members (member_ID) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 70000000;

CREATE TABLE IF NOT EXISTS `BookMark`
(
    `member_ID` int(8) NOT NULL,
    `ISBN`      bigint NOT NULL,
    CONSTRAINT pk PRIMARY KEY (member_ID, ISBN),
    CONSTRAINT FOREIGN KEY (member_ID) REFERENCES Members (member_ID) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (ISBN) REFERENCES Books (ISBN) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `Comments`
(
    `comment_ID` int(8)                              NOT NULL AUTO_INCREMENT,
    `member_ID`  int(8)                              NOT NULL,
    `ISBN`       bigint                              NOT NULL,
    `title`      text,
    `content`    text                                not null,
    `rate`       ENUM ('0', '1', '2', '3', '4', '5') NOT NULL default '5',
    CONSTRAINT pk PRIMARY KEY (comment_ID),
    CONSTRAINT FOREIGN KEY (member_ID) REFERENCES members (member_ID) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (ISBN) REFERENCES Books (ISBN) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 80000000;

CREATE TABLE IF NOT EXISTS `Reports`
(
    `report_ID`    int(8)                       NOT NULL AUTO_INCREMENT,
    `member_ID`    int(8)                       NOT NULL,
    `title`        text,
    `content`      text                         NOT NULL,
    `ReportStatus` ENUM ('PENDING', 'RESOLVED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT pk PRIMARY KEY (report_ID),
    CONSTRAINT FOREIGN KEY (member_ID) REFERENCES members (member_ID) ON UPDATE CASCADE ON DELETE CASCADE
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    AUTO_INCREMENT = 80000000;

DELIMITER $$

-- Trigger cập nhật bảng Books khi có INSERT trong BookItem
CREATE TRIGGER update_books_on_bookitem_insert
    AFTER INSERT
    ON BookItem
    FOR EACH ROW
BEGIN
    DECLARE total_quantity INT;
    DECLARE total_lost INT;
    DECLARE total_loaned INT;
    DECLARE total_reserved INT;

    -- Tính tổng số BookItem có cùng ISBN
    SELECT COUNT(*)
    INTO total_quantity
    FROM BookItem
    WHERE ISBN = NEW.ISBN;

    -- Tính tổng số BookItem có BookItemStatus là 'LOST'
    SELECT COUNT(*)
    INTO total_lost
    FROM BookItem
    WHERE ISBN = NEW.ISBN
      AND BookItem.BookItemStatus = 'LOST';

    -- Tính tổng số BookItem có BookItemStatus là 'LOANED'
    SELECT COUNT(*)
    INTO total_loaned
    FROM BookItem
    WHERE ISBN = NEW.ISBN
      AND BookItemStatus = 'LOANED';

    -- Tính tổng số BookItem có BookItemStatus là 'RESERVED'
    SELECT COUNT(*)
    INTO total_reserved
    FROM BookItem
    WHERE ISBN = NEW.ISBN
      AND BookItemStatus = 'RESERVED';

    -- Cập nhật các giá trị vào bảng Books
    UPDATE Books
    SET quantity             = total_quantity,
        number_lost_book     = total_lost,
        number_loaned_book   = total_loaned,
        number_reserved_book = total_reserved,
        BookStatus           = CASE
                                   WHEN total_quantity <= (total_lost + total_loaned + total_reserved) THEN 'UNAVAILABLE'
                                   ELSE 'AVAILABLE'
            END
    WHERE ISBN = NEW.ISBN;
    END$$

    -- Trigger cập nhật bảng Books khi có UPDATE trong BookItem
    CREATE TRIGGER update_books_on_bookitem_update
        AFTER UPDATE
        ON BookItem
        FOR EACH ROW
    BEGIN
        DECLARE total_quantity INT;
    DECLARE total_lost INT;
    DECLARE total_loaned INT;
    DECLARE total_reserved INT;

    -- Tính tổng số BookItem có cùng ISBN
        SELECT COUNT(*)
        INTO total_quantity
        FROM BookItem
        WHERE ISBN = NEW.ISBN;

        -- Tính tổng số BookItem có BookItemStatus là 'LOST'
        SELECT COUNT(*)
        INTO total_lost
        FROM BookItem
        WHERE ISBN = NEW.ISBN
          AND BookItem.BookItemStatus = 'LOST';

        -- Tính tổng số BookItem có BookItemStatus là 'LOANED'
        SELECT COUNT(*)
        INTO total_loaned
        FROM BookItem
        WHERE ISBN = NEW.ISBN
          AND BookItemStatus = 'LOANED';

        -- Tính tổng số BookItem có BookItemStatus là 'RESERVED'
        SELECT COUNT(*)
        INTO total_reserved
        FROM BookItem
        WHERE ISBN = NEW.ISBN
          AND BookItemStatus = 'RESERVED';

        -- Cập nhật các giá trị vào bảng Books
        UPDATE Books
        SET quantity             = total_quantity,
            number_lost_book     = total_lost,
            number_loaned_book   = total_loaned,
            number_reserved_book = total_reserved,
            BookStatus           = CASE
                                       WHEN total_quantity <= (total_lost + total_loaned + total_reserved) THEN 'UNAVAILABLE'
                                       ELSE 'AVAILABLE'
                END
        WHERE ISBN = NEW.ISBN;
        END$$

        -- Trigger cập nhật bảng Books khi có DELETE trong BookItem
        CREATE TRIGGER update_books_on_bookitem_delete
            AFTER DELETE
            ON BookItem
            FOR EACH ROW
        BEGIN
            DECLARE total_quantity INT;
    DECLARE total_lost INT;
    DECLARE total_loaned INT;
    DECLARE total_reserved INT;

    -- Tính tổng số BookItem có cùng ISBN
            SELECT COUNT(*)
            INTO total_quantity
            FROM BookItem
            WHERE ISBN = OLD.ISBN;

            -- Tính tổng số BookItem có BookItemStatus là 'LOST'
            SELECT COUNT(*)
            INTO total_lost
            FROM BookItem
            WHERE ISBN = OLD.ISBN
              AND BookItem.BookItemStatus = 'LOST';

            -- Tính tổng số BookItem có BookItemStatus là 'LOANED'
            SELECT COUNT(*)
            INTO total_loaned
            FROM BookItem
            WHERE ISBN = OLD.ISBN
              AND BookItemStatus = 'LOANED';

            -- Tính tổng số BookItem có BookItemStatus là 'RESERVED'
            SELECT COUNT(*)
            INTO total_reserved
            FROM BookItem
            WHERE ISBN = OLD.ISBN
              AND BookItemStatus = 'RESERVED';

            -- Cập nhật các giá trị vào bảng Books
            UPDATE Books
            SET quantity             = total_quantity,
                number_lost_book     = total_lost,
                number_loaned_book   = total_loaned,
                number_reserved_book = total_reserved,
                BookStatus           = CASE
                                           WHEN total_quantity <= (total_lost + total_loaned + total_reserved) THEN 'UNAVAILABLE'
                                           ELSE 'AVAILABLE'
                    END
            WHERE ISBN = OLD.ISBN;
            END$$

            DELIMITER ;

DELIMITER $$

            -- Trigger cập nhật bảng Books khi có INSERT trong Comments
            CREATE TRIGGER update_books_rate_on_comment_insert
                AFTER INSERT
                ON Comments
                FOR EACH ROW
            BEGIN
                DECLARE average_rate FLOAT;

    -- Tính trung bình cộng của rate từ bảng Comments
                SELECT AVG(CAST(rate AS UNSIGNED))
                INTO average_rate
                FROM Comments
                WHERE ISBN = NEW.ISBN;

                -- Cập nhật rate vào bảng Books (lấy phần nguyên)
                UPDATE Books
                SET rate = FLOOR(average_rate) - 1
                WHERE ISBN = NEW.ISBN;
                END$$

                -- Trigger cập nhật bảng Books khi có UPDATE trong Comments
                CREATE TRIGGER update_books_rate_on_comment_update
                    AFTER UPDATE
                    ON Comments
                    FOR EACH ROW
                BEGIN
                    DECLARE average_rate FLOAT;

    -- Tính trung bình cộng của rate từ bảng Comments
                    SELECT AVG(CAST(rate AS UNSIGNED))
                    INTO average_rate
                    FROM Comments
                    WHERE ISBN = NEW.ISBN;

                    -- Cập nhật rate vào bảng Books (lấy phần nguyên)
                    UPDATE Books
                    SET rate = FLOOR(average_rate) - 1
                    WHERE ISBN = NEW.ISBN;
                    END$$

                    -- Trigger cập nhật bảng Books khi có DELETE trong Comments
                    CREATE TRIGGER update_books_rate_on_comment_delete
                        AFTER DELETE
                        ON Comments
                        FOR EACH ROW
                    BEGIN
                        DECLARE average_rate FLOAT;

    -- Tính trung bình cộng của rate từ bảng Comments
                        SELECT AVG(CAST(rate AS UNSIGNED))
                        INTO average_rate
                        FROM Comments
                        WHERE ISBN = OLD.ISBN;

                        -- Cập nhật rate vào bảng Books (lấy phần nguyên)
                        UPDATE Books
                        SET rate = FLOOR(average_rate) - 1
                        WHERE ISBN = OLD.ISBN;
                        END$$

                        DELIMITER ;

DELIMITER $$

                        -- Trigger cập nhật trạng thái trong BookReservation và BookItem khi thời gian hiện tại lớn hơn due_date
                        CREATE TRIGGER update_bookreservation_on_due_date
                            AFTER UPDATE
                            ON BookReservation
                            FOR EACH ROW
                        BEGIN
                            IF NEW.BookReservationStatus = 'WAITING' AND DATE(NOW()) > NEW.due_date THEN
        -- Cập nhật trạng thái trong BookReservation
                            UPDATE BookReservation
                            SET BookReservationStatus = 'CANCELED'
                            WHERE barcode = NEW.barcode
                              AND BookReservationStatus = 'WAITING';

                            -- Cập nhật trạng thái trong BookItem
                            UPDATE BookItem
                            SET BookItemStatus = 'AVAILABLE'
                            WHERE barcode = NEW.barcode;
                        END IF;
                        IF NEW.BookReservationStatus = 'CANCELED' THEN
        -- Cập nhật trạng thái trong BookItem
                        UPDATE BookItem
                        SET BookItemStatus = 'AVAILABLE'
                        WHERE barcode = NEW.barcode;
                    END IF;
                    END$$

                    -- Trigger cập nhật trạng thái trong BookItem khi BookReservation có trạng thái là 'WAITING'
                    CREATE TRIGGER update_bookitem_on_bookreservation_waiting
                        AFTER INSERT
                        ON BookReservation
                        FOR EACH ROW
                    BEGIN
                        IF NEW.BookReservationStatus = 'WAITING' THEN
                        UPDATE BookItem
                        SET BookItemStatus = 'RESERVED'
                        WHERE barcode = NEW.barcode;
                    END IF;
                    END$$

                    DELIMITER ;

DELIMITER $$

                    -- Trigger cập nhật trạng thái trong BookItem và BookReservation khi BookIssue có trạng thái là 'BORROWED'
                    CREATE TRIGGER update_bookitem_and_bookreservation_on_borrowed
                        AFTER INSERT
                        ON BookIssue
                        FOR EACH ROW
                    BEGIN
                        IF NEW.BookIssueStatus = 'BORROWED' THEN
        -- Cập nhật trạng thái trong BookItem
                        UPDATE BookItem
                        SET BookItemStatus = 'LOANED'
                        WHERE barcode = NEW.barcode
                          AND (BookItemStatus = 'AVAILABLE' OR BookItemStatus = 'RESERVED');

                        -- Cập nhật trạng thái trong BookReservation
                        UPDATE BookReservation
                        SET BookReservationStatus = 'COMPLETED'
                        WHERE barcode = NEW.barcode
                          AND BookReservationStatus = 'WAITING';
                    END IF;
                    END$$

                    -- Trigger cập nhật trạng thái trong BookItem và BookIssue khi thời gian hiện tại lớn hơn due_date 1 tháng
                    CREATE TRIGGER update_status_on_due_date_expiration
                        AFTER UPDATE
                        ON BookIssue
                        FOR EACH ROW
                    BEGIN
                        IF NEW.BookIssueStatus = 'LOANED' AND DATE(NOW()) > DATE_ADD(NEW.due_date, INTERVAL 1 MONTH) THEN
        -- Cập nhật trạng thái trong BookIssue
                        UPDATE BookIssue
                        SET BookIssueStatus = 'LOST'
                        WHERE barcode = NEW.barcode;

                        -- Cập nhật trạng thái trong BookItem
                        UPDATE BookItem
                        SET BookItem.BookItemStatus = 'LOST'
                        WHERE barcode = NEW.barcode;
                    END IF;

                    -- Cập nhật trạng thái trong BookItem nếu BookIssue có trạng thái 'RETURNED'
                    IF NEW.BookIssueStatus = 'RETURNED' THEN
                    UPDATE BookItem
                    SET BookItem.BookItemStatus = 'AVAILABLE'
                    WHERE barcode = NEW.barcode;
                END IF;

                IF NEW.BookIssueStatus = 'LOST' THEN
                UPDATE BookItem
                SET BookItemStatus = 'LOST'
                WHERE barcode = NEW.barcode;
            END IF;
            END$$

            DELIMITER ;

            CREATE INDEX idx_books_isbn ON Books (ISBN);
            CREATE INDEX idx_books_title ON Books (title);
            CREATE INDEX idx_books_rate ON Books (rate);

            CREATE INDEX idx_authors_author_name ON Authors (author_name);

            CREATE INDEX idx_category_category_name ON Category (category_name);

            CREATE INDEX idx_bookitem_isbn ON BookItem (ISBN);
            CREATE INDEX idx_bookitem_status ON BookItem (BookItemStatus);

            CREATE INDEX idx_members_email ON Members (email);
            CREATE INDEX idx_members_phone ON Members (phone);
            CREATE INDEX idx_members_first_name ON Members (first_name);
            CREATE INDEX idx_members_last_name ON Members (last_name);

            CREATE INDEX idx_users_username ON Users (username);
            CREATE INDEX idx_users_status ON Users (AccountStatus);

            CREATE INDEX idx_bookreservation_member_id ON BookReservation (member_ID);
            CREATE INDEX idx_bookreservation_barcode ON BookReservation (barcode);
            CREATE INDEX idx_bookreservation_status ON BookReservation (BookReservationStatus);

            CREATE INDEX idx_bookissue_member_id ON BookIssue (member_ID);
            CREATE INDEX idx_bookissue_barcode ON BookIssue (barcode);
            CREATE INDEX idx_bookissue_status ON BookIssue (BookIssueStatus);

            CREATE INDEX idx_comments_isbn ON Comments (ISBN);
            CREATE INDEX idx_comments_member_id ON Comments (member_ID);

            CREATE INDEX idx_reports_member_id ON Reports (member_ID);
            CREATE INDEX idx_reports_status ON Reports (ReportStatus);
