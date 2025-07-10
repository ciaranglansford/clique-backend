### API Endpoints

#### PotController (`/api/pots`)

- **GET** `/api/pots/all`  
  _Returns all contract addresses for existing pots._  
  **Response:**
  ```json
  {
    "potList": [
      "0x123...",
      "0x456..."
    ]
  }
  ```

- **POST** `/api/pots/create`  
  _Creates a new pot with a given contract address._  
  **Request Body:**
  ```json
  {
    "contractAddress": "0x123..."
  }
  ```
  **Response:**
  ```json
  {
    "contractAddress": "0x123...",
    "createdAt": "2025-06-10T12:34:56.789"
  }
  ```

#### UserController (`/api/user`)

- **GET** `/api/user/list?walletAddress=0xabc...`  
  _Returns all pot contract addresses joined by a user._  
  **Response:**
  ```json
  {
    "potList": [
      "0x123...",
      "0x456..."
    ]
  }
  ```

- **POST** `/api/user/join`  
  _Joins a user to a pot._  
  **Request Body:**
  ```json
  {
    "contractAddress": "0x123...",
    "walletAddress": "0xabc..."
  }
  ```
  **Response:**
  ```json
  {
    "walletAddress": "0xabc...",
    "joinedAt": "2024-06-10T12:34:56.789"
  }
  ```

#### Error Responses

All endpoints may return:
```json
{
  "error": "Error message here"
}
```
with appropriate HTTP status codes (e.g., `400 Bad Request`).