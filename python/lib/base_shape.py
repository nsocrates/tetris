from abc import ABC, abstractmethod

class BaseShapeClass(ABC):
    @abstractmethod
    def is_equal(self, other):
        raise NotImplementedError()
