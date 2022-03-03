from functools import reduce
from math import floor
from utils import flatten
from .base_shape import BaseShapeClass
from .point import Point
import math

class Shape(BaseShapeClass):
    """An immutable representation of a Tetromino shape.

    Attributes:
        next: A reference pointing to the shape of the next rotation - type:Shape
        prev: A reference pointing to the shape of the prev rotation - type:Shape
        body: A list of absolute coordinates of the body - type:list
        pivot: The coordinates of the pivot - type:Point
        color: String representing the shape's color - type:string
        width: The shape's width - type:int
        height: The shape's height - type:int
        skirt: Lists of the lowest x value for every y value - type:list
        matrix_size: The size of the rotation matrix - type:int
    """

    @staticmethod
    def add_rotations(this):
        """ Creates a linked list consisting of Shapes with references
            to the previous and next shapes

        Args:
            this: instanceof:Shape
        """
        assert isinstance(this, Shape)
        def make_next_shape(shape):
            body = Shape.compute_rotation(shape, 1)
            return Shape(body, this.pivot, this.color, this.name)

        def generate_shapes(shape):
            next_shape = make_next_shape(shape)
            while not shape.is_equal(next_shape):
                curr_shape = next_shape
                next_shape = make_next_shape(next_shape)
                yield curr_shape

        prev_shape = this
        for shape in generate_shapes(this):
            shape.prev = prev_shape
            prev_shape.next = shape
            prev_shape = shape
        this.prev = prev_shape
        prev_shape.next = this
        return this

    @staticmethod
    def readjust_body(body):
        """Readjusts the body closest to top-left (0,0)
        """
        closest = math.inf
        closest_point = Point(0, 0)

        for p in body:
            d = p.distance(0, 0)

            if closest == d:
                closest_point = Point(0, 0)
                break

            if closest > d:
                closest = d
                closest_point = p

        return [p - closest_point for p in body]

    @staticmethod
    def compute_dimensions(body):
        width = 0
        height = 0
        for p in body:
            if p.x > width:
                width = p.x
            if p.y > height:
                height = p.y

        return [width + 1, height + 1]

    @staticmethod
    def compute_skirt(body, width, height):
        """Stores the lowest Y value for each X value."""
        skirt = [height] * width
        for p in body:
            # Flip Y so that (0,0) is at bottom left
            y = -p.y + 1
            if skirt[p.x] > y:
                skirt[p.x] = y
        return skirt

    @staticmethod
    def compute_rotation_size(body):
        return max(Shape.compute_dimensions(body))

    @staticmethod
    def compute_rotation(shape, dir_):
        a = []
        px,py = shape.pivot
        for b in shape.body:
            x = int(px + dir_*py - dir_*b.y)
            y = int(py - dir_*px + dir_*b.x)
            a.append(Point(x, y))
        return a

    def __init__(self, body, pivot, color, name):
        body_at_origin = Shape.readjust_body(body)
        self.next = None
        self.prev = None
        self.body = body
        self.pivot = pivot
        self.color = color
        self.name = name
        self.rotation_size = Shape.compute_rotation_size(body)
        self.width, self.height = Shape.compute_dimensions(body_at_origin)
        self.skirt = Shape.compute_skirt(body_at_origin, self.width, self.height)

    def __str__(self):
        s = ""
        for i in range(self.rotation_size):
            for j in range(self.rotation_size):
                s += self.color if (j, i) in self.body else "."
                s += " "
            s += "\n"
        return s.strip()

    def __repr__(self):
        return str(self.body)

    def clone(self):
        """Returns a copy of itself
        """
        return Shape(self.body, self.pivot, self.color, self.name)

    def is_equal(self, other):
        """Checks if given shape is the same as current shape
        """
        if self == other:
            return True

        if not isinstance(other, Shape):
            return False

        s1 = set(map(tuple, self.body))
        s2 = set(map(tuple, other.body))
        return not s1.symmetric_difference(s2)
