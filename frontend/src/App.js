import React, { useState, useEffect } from 'react';
import { Container, Button, Card, Row, Col, Badge, Form } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
    const [movies, setMovies] = useState([]);

    const [formData, setFormData] = useState({ title: '', director: '', genre: '', rating: '' });

    const handleFetchData = () => {
        fetch('http://localhost:8080/JavaSpring/user', {
            method: 'GET',
            credentials: 'include'
        })
            .then(res => res.json())
            .then(data => setMovies(data))
            .catch(err => console.error("Помилка GET:", err));
    };

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleAddMovie = (e) => {
        e.preventDefault();

        const params = new URLSearchParams();
        params.append('title', formData.title);
        params.append('director', formData.director);
        params.append('genre', formData.genre);
        params.append('rating', formData.rating);

        fetch('http://localhost:8080/JavaSpring/user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            credentials: 'include',
            body: params.toString()
        })
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    handleFetchData(); // Миттєво оновлюємо список!
                    setFormData({ title: '', director: '', genre: '', rating: '' }); // Очищаємо форму
                } else {
                    alert("Помилка: " + data.message);
                }
            })
            .catch(err => console.error("Помилка POST:", err));
    };

    return (
        <Container className="mt-5">
            <h1 className="text-center mb-4">Каталог Фільмів</h1>

            {/* Форма додавання нового фільму */}
            <Row className="justify-content-center mb-5">
                <Col md={8}>
                    <Card className="shadow-sm">
                        <Card.Header className="bg-dark text-white fw-bold">Додати новий фільм</Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleAddMovie}>
                                <Row>
                                    <Col md={6} className="mb-3">
                                        <Form.Control type="text" name="title" placeholder="Назва фільму" value={formData.title} onChange={handleInputChange} required />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                        <Form.Control type="text" name="director" placeholder="Режисер" value={formData.director} onChange={handleInputChange} required />
                                    </Col>
                                    <Col md={6} className="mb-3">
                                        <Form.Select name="genre" value={formData.genre} onChange={handleInputChange} required>
                                            <option value="">Оберіть жанр...</option>
                                            <option value="Фантастика">Фантастика</option>
                                            <option value="Бойовик">Бойовик</option>
                                            <option value="Драма">Драма</option>
                                            <option value="Комедія">Комедія</option>
                                            <option value="Жахи">Жахи</option>
                                        </Form.Select>
                                    </Col>
                                    <Col md={4} className="mb-3">
                                        <Form.Control type="number" step="0.1" max="10" name="rating" placeholder="Рейтинг (напр. 8.5)" value={formData.rating} onChange={handleInputChange} required />
                                    </Col>
                                    <Col md={2}>
                                        <Button variant="success" type="submit" className="w-100">Додати</Button>
                                    </Col>
                                </Row>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row className="justify-content-center mb-4">
                <Col md={8} className="d-flex justify-content-center">
                    <Button variant="outline-dark" onClick={handleFetchData}>
                        Отримати дані з БД
                    </Button>
                </Col>
            </Row>

            {/* Список фільмів */}
            <Row>
                {movies.map(item => (
                    <Col md={4} sm={6} key={item.id} className="mb-4">
                        <Card className="shadow-sm h-100 border-0">
                            <Card.Body>
                                <Card.Title className="fw-bold">{item.title}</Card.Title>
                                <Badge bg="secondary" className="mb-3">{item.genre}</Badge>
                                <Card.Text>
                                    <strong>Режисер:</strong> <span className="text-muted">{item.director}</span><br/>
                                </Card.Text>
                            </Card.Body>
                            <Card.Footer className="bg-white border-0 text-end">
                                <span className="text-warning fw-bold fs-5">★ {item.rating.toFixed(1)}</span>
                            </Card.Footer>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    );
}

export default App;