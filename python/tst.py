from timeit import timeit
width = 10
height = 22

_matrix = [["."] * width for _ in range(height)]
d = {}


def rmv(matrix):
    """Print the grid with (0, 0) as the bottom left
    """
    for m in range(height):
        if all(["." != b for b in matrix[m]]):
            matrix[m] = ["."] * width
    return matrix

def pnt(matrix):
    rmv(matrix)
    g = ""
    for i in reversed(range(height)):
        g += " ".join([matrix[i][j] for j in range(width)])
        g += "\n"
    return g

print(timeit('[n for n in range(100)]', number=10000))
print(timeit('[n for n in range(100)].reverse()', number=10000))
print(timeit('[n for n in range(100)][::-1]', number=10000))
print(timeit('reversed([n for n in range(100)])', number=10000))
