import React, { useState, useEffect } from 'react';
import { Container, Button, Card, Row, Col, Badge, Form } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
    const [movies, setMovies] = useState([]);
    const [formData, setFormData] = useState({ title: '', director: '', genre: '', rating: '' });
    const [editingId, setEditingId] = useState(null);
    const [searchId, setSearchId] = useState('');

    const handleFetchData = () => {
        fetch('http://localhost:8080/JavaSpring/user', {
            method: 'GET',
            credentials: 'include'
        })
            .then(res => res.json())
            .then(data => setMovies(data))
            .catch(err => console.error("Помилка GET:", err));
    };

    const handleSearch = () => {
        if (!searchId) {
            handleFetchData();
            return;
        }
        fetch(`http://localhost:8080/JavaSpring/user?id=${searchId}`, {
            method: 'GET',
            credentials: 'include'
        })
            .then(res => res.json())
            .then(data => setMovies(data))
            .catch(err => console.error("Помилка пошуку:", err));
    };

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const params = new URLSearchParams();
        params.append('title', formData.title);
        params.append('director', formData.director);
        params.append('genre', formData.genre);
        params.append('rating', formData.rating);

        if (editingId) {
            params.append('id', editingId);
            fetch(`http://localhost:8080/JavaSpring/user?${params.toString()}`, {
                method: 'PUT',
                credentials: 'include'
            })
                .then(res => res.json())
                .then(data => {
                    if (data.status === 'success') {
                        handleFetchData();
                        setFormData({ title: '', director: '', genre: '', rating: '' });
                        setEditingId(null);
                    } else alert("Помилка: " + data.message);
                });
        } else {
            fetch('http://localhost:8080/JavaSpring/user', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                credentials: 'include',
                body: params.toString()
            })
                .then(res => res.json())
                .then(data => {
                    if (data.status === 'success') {
                        handleFetchData();
                        setFormData({ title: '', director: '', genre: '', rating: '' });
                    } else alert("Помилка: " + data.message);
                });
        }
    };

    const handleEditClick = (movie) => {
        setFormData({ title: movie.title, director: movie.director, genre: movie.genre, rating: movie.rating });
        setEditingId(movie.id);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const handleDelete = (id) => {
        if(window.confirm("Дійсно видалити цей фільм?")) {
            fetch(`http://localhost:8080/JavaSpring/user?id=${id}`, {
                method: 'DELETE',
                credentials: 'include'
            })
                .then(res => res.json())
                .then(data => {
                    if (data.status === 'success') handleFetchData();
                    else alert("Помилка: " + data.message);
                });
        }
    };

    const cancelEdit = () => {
        setFormData({ title: '', director: '', genre: '', rating: '' });
        setEditingId(null);
    };

    return (
        <Container className="mt-5">
            <h1 className="text-center mb-4">Каталог Фільмів</h1>

            <Row className="justify-content-center mb-5">
                <Col md={8}>
                    <Card className="shadow-sm border-0 bg-light">
                        <Card.Header className={editingId ? "bg-warning fw-bold" : "bg-dark text-white fw-bold"}>
                            {editingId ? "Редагувати фільм" : "Додати новий фільм"}
                        </Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={6} className="mb-3">
                                        <Form.Control type="text" name="title" placeholder="Назва" value={formData.title} onChange={handleInputChange} required />
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
                                            <option value="Кримінал">Кримінал</option>
                                        </Form.Select>
                                    </Col>
                                    <Col md={4} className="mb-3">
                                        <Form.Control type="number" step="0.1" max="10" name="rating" placeholder="Рейтинг" value={formData.rating} onChange={handleInputChange} required />
                                    </Col>
                                    <Col md={12} className="d-flex justify-content-end gap-2 mt-2">
                                        {editingId && <Button variant="secondary" onClick={cancelEdit}>Скасувати</Button>}
                                        <Button variant={editingId ? "warning" : "success"} type="submit">
                                            {editingId ? "Зберегти зміни" : "Додати"}
                                        </Button>
                                    </Col>
                                </Row>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row className="justify-content-center mb-4">
                <Col md={8}>
                    <Card className="shadow-sm border-0 p-3">
                        <Row>
                            <Col md={5} className="mb-2 mb-md-0">
                                <Button variant="outline-dark" className="w-100" onClick={handleFetchData}>
                                    Отримати всі дані
                                </Button>
                            </Col>
                            <Col md={5} className="mb-2 mb-md-0">
                                <Form.Control type="number" placeholder="Введіть ID для пошуку" value={searchId} onChange={(e) => setSearchId(e.target.value)} />
                            </Col>
                            <Col md={2}>
                                <Button variant="info" className="w-100 text-white" onClick={handleSearch}>
                                    Знайти
                                </Button>
                            </Col>
                        </Row>
                    </Card>
                </Col>
            </Row>

            <Row>
                {movies.map(item => (
                    <Col md={4} sm={6} key={item.id} className="mb-4">
                        <Card className="shadow-sm h-100 border-0">
                            <Card.Body>
                                <Card.Title className="fw-bold">{item.title}</Card.Title>
                                <Badge bg="secondary" className="mb-3">{item.genre}</Badge>
                                <Card.Text>
                                    <strong>Режисер:</strong> <span className="text-muted">{item.director}</span><br/>
                                    <small className="text-muted">ID: {item.id}</small>
                                </Card.Text>
                            </Card.Body>
                            <Card.Footer className="bg-white border-0 d-flex justify-content-between align-items-center">
                                <span className="text-warning fw-bold fs-5">★ {item.rating.toFixed(1)}</span>
                                <div>
                                    <Button variant="outline-primary" size="sm" className="me-2" onClick={() => handleEditClick(item)}>
                                        ✎
                                    </Button>
                                    <Button variant="outline-danger" size="sm" onClick={() => handleDelete(item.id)}>
                                        ✖
                                    </Button>
                                </div>
                            </Card.Footer>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    );
}

export default App;