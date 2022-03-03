from math import fabs, sqrt

class Point:
    @classmethod
    def from_matrix(cls, matrix):
        return [cls(ay, ax) for (ax, ay) in matrix]

    @classmethod
    def from_list(cls, lst):
        return [cls(px, py) for (px, py) in lst]

    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y

    def __iter__(self):
        yield self.x
        yield self.y

    def __str__(self):
        return '({self.x}, {self.y})'.format(self=self)

    __repr__ = __str__

    def __eq__(self, other):
        x,y = other
        return self.x == x and self.y == y

    def __add__(self, other):
        x,y = other
        return Point(self.x + x, self.y + y)

    def __sub__(self, other):
        x,y = other
        return Point(self.x - x, self.y - y)

    def add(self, dx, dy):
        return (self.x + dx, self.y + dy)

    def sub(self, dx, dy):
        return (self.x - dx, self.y - dy)

    def distance(self, dx, dy):
        return sqrt((self.x-dx)**2+(self.y-dy)**2)

    def move(self, dx, dy):
        x,y = self.add(dx, dy)
        self.x = x
        self.y = y
        return (self.x, self.y)

    def get(self):
        return (self.x, self.y)

    def set(self, dx, dy):
        self.x = dx
        self.y = dy
        return (self.x, self.y)
