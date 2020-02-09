import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ItemCreateComponent } from './item-create/item-create.component';
import { ItemEditComponent } from './item-edit/item-edit.component';
import { ItemListComponent } from './item-list/item-list.component';


const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'items-list' },
  { path: 'create-item', component: ItemCreateComponent },
  { path: 'items-list', component: ItemListComponent },
  { path: 'item-edit/:id', component: ItemEditComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
