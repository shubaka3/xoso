package hutech.com.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "numbers")
public class Number {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id;

    private   Long valuenumber;


    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
//    @Temporal(TemporalType.DATE)
//    private LocalDate currentDate = LocalDate.now();
//    @Temporal(TemporalType.DATE)
//    private  Date date;


}
