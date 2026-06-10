package pl.kmalski.verification.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationUseCase;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationUseCase;
import pl.kmalski.verification.infrastructure.web.dto.GetVerificationResponse;
import pl.kmalski.verification.infrastructure.web.dto.StartVerificationRequest;
import pl.kmalski.verification.infrastructure.web.dto.StartVerificationResponse;
import pl.kmalski.verification.infrastructure.web.mapper.VerificationDtoMapper;

import java.util.UUID;

@RestController
@RequestMapping("/verifications")
@RequiredArgsConstructor
public class VerificationController {

    private final StartVerificationUseCase startVerificationUseCase;
    private final GetVerificationUseCase getVerificationUseCase;
    private final VerificationDtoMapper verificationDtoMapper;

    @PostMapping
    public StartVerificationResponse startVerification(@RequestBody StartVerificationRequest request) {
        var command = verificationDtoMapper.toStartVerificationCommand(request);
        var result = startVerificationUseCase.start(command);
        return verificationDtoMapper.toStartVerificationResponse(result);
    }

    @GetMapping("/{id}")
    public GetVerificationResponse getVerification(@PathVariable UUID id) {
        var query = verificationDtoMapper.toGetVerificationQuery(id);
        var result = getVerificationUseCase.get(query);
        return verificationDtoMapper.toGetVerificationResponse(result);
    }

}
