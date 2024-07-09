package hutech.com.demo.service;
import hutech.com.demo.model.Number;
import hutech.com.demo.repository.NumberRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class NumberService {
    private final NumberRepository numberRepository;

    public List<Number> getAllNumbers() {
        return numberRepository.findAll();
    }

    public Optional<Number> getNumberById(Long id) {
        return numberRepository.findById(id);
    }

    public void addNumber(Number number) {
        numberRepository.save(number);
    }

    public void updateNumber(@NotNull Number number) {
        Number existingNumber = numberRepository.findById(number.getId())
                .orElseThrow(() -> new IllegalStateException("Number with ID " + number.getId() + " does not exist."));
        existingNumber.setValuenumber(number.getValuenumber());
        numberRepository.save(existingNumber);
    }

    public void deleteNumberById(Long id) {
        if (!numberRepository.existsById(id)) {
            throw new IllegalStateException("Number with ID " + id + " does not exist.");
        }
        numberRepository.deleteById(id);
    }
    public int generateRandomFiveDigitNumber() {

        Random random = new Random();
        return 10000 + random.nextInt(90000); // Generates a random number between 10000 and 99999
    }

}
