$baseUrl = "http://localhost:8080"
$timeoutSeconds = 60
$startedAt = Get-Date

$startBody = @{
    payment = @{
        paymentId  = "payment-1"
        customerId = "customer-1"
        amount     = "100.00"
        currency   = "PLN"
        country    = "PL"
    }
} | ConvertTo-Json -Depth 10

Write-Host "POST $baseUrl/v1/verifications"
Write-Host "Request:"
Write-Host $startBody
Write-Host ""

try {
    $startResponse = Invoke-RestMethod -Method Post `
        -Uri "$baseUrl/v1/verifications" `
        -ContentType "application/json" `
        -Body $startBody
} catch {
    Write-Host "Request failed."
    Write-Host $_.Exception.Message
    throw
}

Write-Host "Response:"
$startResponse | ConvertTo-Json -Depth 10 | Write-Host
Write-Host ""

$verificationId = $startResponse.verificationId

while ($true) {
    $getEndpoint = "$baseUrl/v1/verifications/$verificationId"
    Write-Host "GET $getEndpoint"

    $getResponse = Invoke-RestMethod -Method Get `
        -Uri $getEndpoint

    Write-Host "Response:"
    $getResponse | ConvertTo-Json -Depth 10 | Write-Host
    Write-Host ""

    $status = $getResponse.status

    if ($status -in @("COMPLETED", "FAILED")) {
        break
    }

    if (((Get-Date) - $startedAt).TotalSeconds -ge $timeoutSeconds) {
        throw "Timed out after $timeoutSeconds seconds waiting for verification $verificationId"
    }

    Start-Sleep -Seconds 1
}
