# db-to-json
Springboot project for DB to json


#API Details:

  1. To Save a loan

     Method: POST

     URL: http://localhost:8080/api/loans
  
     Body:
    
    ```json
    {
      "loanNo": "LN009",
      "applicationNo": "APP009",
      "customer": {
        "customerNo": "CUST009",
        "firstName": "Grace",
        "lastName": "Hopper",
        "city": "Chennai",
        "mobileNo": "9012345690",
        "rep": false
      },
      "amount": 120000,
      "disbursedOn": "2025-05-25",
      "totalTenure": 12,
      "emi": 10500,
      "emiPayments": [
        {
          "loanNo": "LN009",
          "instalment": 1,
          "dueDate": "2025-06-25",
          "emi": 10500,
          "status": "Pending",
          "paymentReferences": []
        },
        {
          "loanNo": "LN009",
          "instalment": 2,
          "dueDate": "2025-07-25",
          "emi": 10500,
          "status": "Pending",
          "paymentReferences": []
        }
      ]
    }

  2. To fetch loan details

     Method: GET

     URL: http://localhost:8080/api/loans/{loanNo}

     Example: http://localhost:8080/api/loans/LN009

     Respnse:
     
     Loan details fetched and written to file successfully for loan number: LN009
