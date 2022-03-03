from utils import pick
from .shape import Shape
from .point import Point

class IShape(Shape):
    def __init__(self):
        name = "I"
        color = "c"
        body = Point.from_matrix([
            (1, 0),
            (1, 1),
            (1, 2),
            (1, 3),
        ])
        pivot = Point(1.5, 1.5)
        Shape.__init__(self, body, pivot, color, name)


class OShape(Shape):
    def __init__(self):
        name = "O"
        color = "y"
        body = Point.from_matrix([
            (0, 0),
            (0, 1),
            (1, 0),
            (1, 1),
        ])
        pivot = Point(0.5, 0.5)
        Shape.__init__(self, body, pivot, color, name)


class ZShape(Shape):
    def __init__(self):
        name = "Z"
        color = "r"
        body = Point.from_matrix([
            (0, 0),
            (0, 1),
            (1, 1),
            (1, 2),
        ])
        pivot = Point(1, 1)
        Shape.__init__(self, body, pivot, color, name)


class SShape(Shape):
    def __init__(self):
        name = "S"
        color = "g"
        body = Point.from_matrix([
            (0, 1),
            (0, 2),
            (1, 0),
            (1, 1),
        ])
        pivot = Point(1, 1)
        Shape.__init__(self, body, pivot, color, name)


class JShape(Shape):
    def __init__(self):
        name = "J"
        color = "b"
        body = Point.from_matrix([
            (0, 0),
            (1, 0),
            (1, 1),
            (1, 2),
        ])
        pivot = Point(1, 1)
        Shape.__init__(self, body, pivot, color, name)


class LShape(Shape):
    def __init__(self):
        name = "L"
        color = "o"
        body = Point.from_matrix([
            (0, 2),
            (1, 0),
            (1, 1),
            (1, 2),
        ])
        pivot = Point(1, 1)
        Shape.__init__(self, body, pivot, color, name)


class TShape(Shape):
    def __init__(self):
        name = "T"
        color = "m"
        body = Point.from_matrix([
            (0, 1),
            (1, 0),
            (1, 1),
            (1, 2),
        ])
        pivot = Point(1, 1)
        Shape.__init__(self, body, pivot, color, name)


class ShapeProxy:
    SHAPES = [IShape, OShape, ZShape, SShape, JShape, LShape, TShape]

    def __init__(self):
        self.shapes = {}
        self.current = None
        self.previous = None
        self.hold_shape = None
        for TetrisPiece in self.SHAPES:
            piece = TetrisPiece()
            self.shapes[piece.name] = Shape.add_rotations(piece)

    def __str__(self):
        return str(self.current)

    def __getattr__(self, name):
        return getattr(self.current, name)

    def get(self, shape):
        if isinstance(shape, Shape):
            return shape

        return self.shapes.get(shape)

    def set(self, shape):
        self.previous = self.current
        self.current = self.get(shape)
        return self.current

    def hold(self, shape):
        self.hold_shape = self.get(shape)
        return self.hold_shape
