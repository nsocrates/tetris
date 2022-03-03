from copy import deepcopy

class Grid():
    def __init__(self, width, height, default="."):
        self.width = width
        self.height = height
        self.default = default
        self.matrix = [[default] * width for _ in range(height)]

    def __str__(self):
        g = ""
        for i in range(self.height):
            g += " ".join([self.matrix[i][j] for j in range(self.width)])
            if i < self.height:
                g += "\n"
        return g.strip()

    def get(self, x, y):
        return self.matrix[y][x]

    def set(self, x, y, value):
        self.matrix[y][x] = value

    def get_row(self, y):
        return self.matrix[y]

    def set_row(self, y, values):
        self.matrix[y] = values

    def clear(self):
        self.matrix = [[self.default] * self.width for _ in range(self.height)]

    def replace(self, new_matrix):
        assert len(new_matrix) == self.height and len(new_matrix[0]) == self.width
        self.matrix = new_matrix

    def clone(self):
        grid = Grid(self.width, self.height, self.default)
        grid.replace(deepcopy(self.matrix))
        return grid
