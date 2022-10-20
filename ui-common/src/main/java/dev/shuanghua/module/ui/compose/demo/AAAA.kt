package dev.shuanghua.module.ui.compose.demo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PopularBooksDemo() {
    var comparator: Comparator<Book> by remember { mutableStateOf(TitleComparator) }
    Column(modifier = Modifier.statusBarsPadding()) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Title",
                Modifier
                    .clickable { comparator = TitleComparator }
                    .weight(5f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
            Text(
                "Author",
                Modifier
                    .clickable { comparator = AuthorComparator }
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
            Text(
                "Year",
                Modifier
                    .clickable { comparator = YearComparator }
                    .width(50.dp)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
            Text(
                "Sales (M)",
                Modifier
                    .clickable { comparator = SalesComparator }
                    .width(65.dp)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
        Divider(color = Color.LightGray, thickness = Dp.Hairline)
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val sortedList = PopularBooksList.sortedWith(comparator)
            items(sortedList, key = { it.title }) {
                Row(
                    Modifier
                        .animateItemPlacement()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        it.title,
                        Modifier
                            .weight(5f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        it.author,
                        Modifier
                            .weight(2f)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "${it.published}",
                        Modifier
                            .width(55.dp)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "${it.salesInMillions}",
                        Modifier
                            .width(65.dp)
                            .fillMaxHeight()
                            .padding(4.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private val TitleComparator = Comparator<Book> { left, right ->
    left.title.compareTo(right.title)
}

private val AuthorComparator = Comparator<Book> { left, right ->
    left.author.compareTo(right.author)
}

// 根据Year 大小进行排序
private val YearComparator = Comparator<Book> { left, right ->
    right.published.compareTo(left.published)
}

private val SalesComparator = Comparator<Book> { left, right ->
    right.salesInMillions.compareTo(left.salesInMillions)
}

private val PopularBooksList = listOf(
    Book("The Hobbit", "J. R. R. Tolkien", 1937, 140),
    Book("Harry Potter and the Philosopher's Stone", "J. K. Rowling", 1997, 120),
    Book("Dream of the Red Chamber", "Cao Xueqin", 1800, 100),
    Book("And Then There Were None", "Agatha Christie", 1939, 100),
    Book("The Little Prince", "Antoine de Saint-Exupéry", 1943, 100),
    Book("The Lion, the Witch and the Wardrobe", "C. S. Lewis", 1950, 85),
    Book("The Adventures of Pinocchio", "Carlo Collodi", 1881, 80),
    Book("The Da Vinci Code", "Dan Brown", 2003, 80),
    Book("Harry Potter and the Chamber of Secrets", "J. K. Rowling", 1998, 77),
    Book("The Alchemist", "Paulo Coelho", 1988, 65),
    Book("Harry Potter and the Prisoner of Azkaban", "J. K. Rowling", 1999, 65),
    Book("Harry Potter and the Goblet of Fire", "J. K. Rowling", 2000, 65),
    Book("Harry Potter and the Order of the Phoenix", "J. K. Rowling", 2003, 65)
)

private class Book(
    val title: String,
    val author: String,
    val published: Int,
    val salesInMillions: Int
)