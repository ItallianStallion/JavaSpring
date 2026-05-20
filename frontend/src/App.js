import React, { useState } from 'react';
import { Container, Button, Card, Row, Col, Alert } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const [backendData, setBackendData] = useState(null);
  const [message, setMessage] = useState('');

  // 1. Відправка POST-запиту для створення сесії та кукі
  const handleStartSession = () => {
    // credentials: 'include' дозволяє браузеру зберегти кукі, які прийдуть від Tomcat (порт 8080)
    fetch('http://localhost:8080/JavaSpring/user', {
      method: 'POST',
      credentials: 'include'
    })
        .then(res => res.json())
        .then(data => {
          setMessage(data.message);
          setBackendData(null);
        })
        .catch(err => console.error("Помилка POST:", err));
  };

  // 2. Відправка GET-запиту (передаємо @RequestParam та @PathVariable)
  const handleFetchData = () => {

    fetch('http://localhost:8080/JavaSpring/user/ID_Mykola_WSL?param=ReactApp&format=json', {
      method: 'GET',
      credentials: 'include'
    })
        .then(res => res.json())
        .then(data => setBackendData(data))
        .catch(err => console.error("Помилка GET:", err));
  };

  return (
      <Container className="mt-5">
        <h1 className="text-center mb-4">Лабораторна робота №1</h1>

        <Row className="justify-content-center mb-4">
          <Col md={8} className="d-flex justify-content-around">
            <Button variant="primary" size="lg" onClick={handleStartSession}>
              1. Ініціалізувати сесію (POST)
            </Button>
            <Button variant="success" size="lg" onClick={handleFetchData}>
              2. Отримати дані з сервера (GET)
            </Button>
          </Col>
        </Row>

        {message && (
            <Row className="justify-content-center">
              <Col md={6}>
                <Alert variant="info" onClose={() => setMessage('')} dismissible>
                  {message}
                </Alert>
              </Col>
            </Row>
        )}

        {backendData && (
            <Row className="justify-content-center">
              <Col md={6}>
                <Card className="shadow">
                  <Card.Header as="h5" className="bg-dark text-white">
                    Дані з Java-бекенду (JSON format)
                  </Card.Header>
                  <Card.Body>
                    <Card.Text>
                      <strong>@RequestParam (param):</strong> <span className="text-primary">{backendData.requestParam}</span>
                    </Card.Text>
                    <Card.Text>
                      <strong>@PathVariable (з URL):</strong> <span className="text-success">{backendData.pathVariable}</span>
                    </Card.Text>
                    <Card.Text>
                      <strong>Користувач із сесії Tomcat:</strong> <span className="text-danger">{backendData.sessionUser}</span>
                    </Card.Text>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
        )}
      </Container>
  );
}

export default App;